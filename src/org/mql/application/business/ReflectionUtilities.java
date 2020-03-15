package org.mql.application.business;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class ReflectionUtilities {
	private static final String PREFIX = "bin";
	private static final String SUFFIX = ".class";

	/*
	 * Interdire l'instanciation de la class, car elle ne contient que les méthodes
	 * static
	 */
	private ReflectionUtilities() {
	}

	public static boolean isAbstract(Method m) {
		return Modifier.isAbstract(m.getModifiers());
	}

	public static boolean isAbstract(Class<?> c) {
		return Modifier.isAbstract(c.getModifiers());
	}

	public static boolean isFinal(Field f) {
		return Modifier.isFinal(f.getModifiers());
	}

	public static boolean isFinal(Method m) {
		return Modifier.isFinal(m.getModifiers());
	}

	public static boolean isStatic(Field f) {
		return Modifier.isStatic(f.getModifiers());
	}

	public static boolean isStatic(Method m) {
		return Modifier.isStatic(m.getModifiers());
	}

	public static char getVisibility(Field f) {
		return visibility(f.getModifiers());
	}

	public static char getVisibility(Method m) {
		return visibility(m.getModifiers());
	}

	private static char visibility(int modifiers) {
		if (Modifier.isPrivate(modifiers)) {
			return '-';
		} else if (Modifier.isPublic(modifiers)) {
			return '+';
		} else if (Modifier.isProtected(modifiers)) {
			return '#';
		} else {
			return '~';
		}
	}

	public static String getDeclaration(Type genericType) {
		// Map<Integer,Vector<String>>>

		if (genericType instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) genericType;
			StringBuilder declaration = new StringBuilder();
			declaration.append(((Class<?>) parameterizedType.getRawType()).getSimpleName());
			declaration.append("<");
			Type[] typeArgs = parameterizedType.getActualTypeArguments();
			for (int i = 0; i < typeArgs.length; i++) {
				Type typeArg = typeArgs[i];
				if (i > 0) {
					declaration.append(", ");
				}
				declaration.append(getDeclaration(typeArg));
			}

			declaration.append(">");
//			return declaration.toString().replace('$', '.');
			return declaration.toString();

		} else if (genericType instanceof Class<?>) {
			return ((Class<?>) genericType).getSimpleName();
		} else {
			return genericType.getTypeName();
		}
	}

	/*
	 * Return the simple name of the class
	 * 
	 * For a Vector of Strings, this method will return "Vector<String>"
	 */
	public static String getClassName(Class<?> clazz) {
		if (clazz.getTypeParameters().length > 0) {
			StringBuffer buffer = new StringBuffer(clazz.getSimpleName());
			buffer.append("<");
			TypeVariable<?>[] parameters = clazz.getTypeParameters();
			for (int i = 0; i < parameters.length; i++) {
				if (i > 0) {
					buffer.append(",");
				}
				buffer.append(parameters[i].getTypeName());
			}
			buffer.append(">");
			return buffer.toString();
		} else {
			return clazz.getSimpleName();
		}
	}

	/*
	 * Return a String representing the Method in the UML convention
	 * 
	 * Example : +setId(int: id) : void
	 */
	public static String getMethodInfo(Method m) {
		StringBuffer methodInfo = new StringBuffer();
		methodInfo.append(getVisibility(m) + m.getName() + "(");

		Parameter[] p = m.getParameters();
		for (int i = 0; i < p.length; i++) {
			if (i > 0) {
				methodInfo.append(", ");
			}
			methodInfo.append(p[i].getName() + ": ");
			methodInfo.append(ReflectionUtilities.getDeclaration(p[i].getParameterizedType()));
		}
		methodInfo.append(") : " + getDeclaration(m.getGenericReturnType()) + System.lineSeparator());

		return methodInfo.toString();
	}

	/*
	 * Return a String representing the Field in the UML convention
	 * 
	 * Example : {@code -id: int}
	 */
	public static String getFieldInfo(Field f) {
		StringBuffer fieldInfo = new StringBuffer();
		fieldInfo.append(getVisibility(f));
		// example : "- long:"
		if (ReflectionUtilities.isFinal(f) && ReflectionUtilities.isStatic(f)) {
			try {
				f.setAccessible(true);
				fieldInfo.append(
						f.getName().toUpperCase()
								+ " : " + ReflectionUtilities.getDeclaration(f.getGenericType())
								+ " = \"" +
//								f.get(null)
								(f.get(null).getClass().isArray() ? Arrays.toString((Object[]) f.get(null))
										: f.get(null))
								+ "\"");
				f.setAccessible(false);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				fieldInfo.append(f.getName() + " : ");
			}
		} else {
			fieldInfo.append(f.getName() + " : " + ReflectionUtilities.getDeclaration(f.getGenericType()));
		}
		return fieldInfo.toString();
	}

	// Exemple : Si on pass en parametre la class String[][][], cette fonction nous
	// renvoie la class String
	public static Class<?> getClassFromArrayClass(Class<?> clazz) {
		if (clazz.isArray()) {
			String className = clazz.getCanonicalName();
			while (className.endsWith("[]")) {
				className = className.substring(0, className.length() - 2);
			}
			try {
				return Class.forName(className);
			} catch (ClassNotFoundException e) {
				return null;
			}
		}
		return clazz;
	}

	/********************/

	public static Set<String> getInternalPackages() {
		Set<File> packages = getInternalJavaPackages(new File(PREFIX));
		Set<String> packagesNames = new HashSet<>();
		for (File file : packages) {
			String packageName = new StringBuffer(file.getPath()).substring(PREFIX.length() + 1)
					.replace(File.separatorChar, '.');
			packagesNames.add(packageName);
		}
		return packagesNames;
	}

	private static Set<File> getInternalJavaPackages(File packageFile) {
		Set<File> javaPackages = new HashSet<>();

		File[] files = packageFile.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.getName().toLowerCase().endsWith(SUFFIX)) {
					javaPackages.add(f.getParentFile());
				} else if (f.isDirectory()) {
					javaPackages.addAll(getInternalJavaPackages(f));
				}
			}
		}
		return javaPackages;
	}

	private static Set<File> getJavaFiles(File packageFile) {
		Set<File> javaFiles = new HashSet<>();

		File[] files = packageFile.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.getName().toLowerCase().endsWith(SUFFIX)) {
					javaFiles.add(f);
				} else if (f.isDirectory()) {
					javaFiles.addAll(getJavaFiles(f));
				}
			}
		}

		return javaFiles;
	}

	public static Set<Class<?>> getInternalClasses(String packageName) {
		File packageFile = new File(
				PREFIX + File.separator + packageName.replace('.', File.separatorChar));

		Set<File> javaFiles = getJavaFiles(packageFile);
		Set<Class<?>> classes = new HashSet<>();
		for (File file : javaFiles) {
			StringBuffer className = new StringBuffer();
			className.append(file.getPath().replace(File.separatorChar, '.'));
			className.delete(0, PREFIX.length() + 1);
			className.delete(className.length() - SUFFIX.length(), className.length());
			try {
				classes.add(Class.forName(className.toString()));
			} catch (ClassNotFoundException e) {
				System.out.println("Erreur : " + e);
			}
		}
		return classes;
	}

	/********************/

	public static Map<Integer, Set<Class<?>>> getClassesByInheritanceLevel(String packageName) {
		return getClassesByInheritanceLevel(getInternalClasses(packageName));
	}

	/*
	 * Return a Map in which every Entry is a level defined by an Integer, and the
	 * value is a Set of Classes of that level
	 */
	public static Map<Integer, Set<Class<?>>> getClassesByInheritanceLevel(Set<Class<?>> allClasses) {
		Map<Integer, Set<Class<?>>> table = new Hashtable<>();
		Set<Class<?>> levelClasses = new HashSet<>();

		// Copy the Set in another to not change it
		Set<Class<?>> remainingClasses = new HashSet<>(allClasses);

		/*
		 * Level 0 : for Object class and primitive classes and interfaces who doesn't
		 * inherit from other interfaces
		 */
		for (Class<?> clazz : allClasses.toArray(new Class<?>[allClasses.size()])) {

			if (
			// Si c'est une annotation ou enum
			clazz.isAnnotation() || clazz.isEnum()
			// Si c'est une classe qui n'hérite pas d'une classes interne (pas dans le Set
			// de classes passé en param), et qu'elle n'implemente pas d'interfaces internes
					|| clazz.getSuperclass() != null
							&& !allClasses.contains(clazz.getSuperclass())
							&& !containsOne(allClasses, clazz.getInterfaces())

					// Si c'est une interface qui n'hérite d'aucune interface, ou qu'elle
					// n'hérite pas d'interfaces internes
					|| clazz.isInterface()
							&& (clazz.getInterfaces().length == 0
									|| !containsOne(allClasses, clazz.getInterfaces()))

					// Si c'est la classe Object
					|| clazz.equals(Object.class)

			) {
				levelClasses.add(clazz);
				remainingClasses.remove(clazz);
			}
		}

		table.put(0, levelClasses);

		// Other levels..
		table.putAll(getClassesLevels(1, remainingClasses, levelClasses));

		return table;
	}

	/*
	 * 'classes' contient les Classes qu'on a pas encore définit leurs niveaux, et
	 * 'supLevelClasses' contient les classes du niveau N-1 , cette fonction
	 * détérmine les classes du niveau N
	 */
	private static Map<Integer, Set<Class<?>>> getClassesLevels(Integer level, Set<Class<?>> classes,
			Set<Class<?>> subLevelClasses) {

		// test d'arrêt
		if (classes.isEmpty()) {
			return Collections.<Integer, Set<Class<?>>>emptyMap();
		}

		Map<Integer, Set<Class<?>>> ordredClasses = new Hashtable<>(classes.size());
		Set<Class<?>> actuelLevelClasses = new HashSet<>();
		Set<Class<?>> remainingClasses = new HashSet<>(classes);

		for (Class<?> clazz : classes) {
			// Si la classe mère de cette classe existe dans les classes niveau N-1, ou bien
			// si cette classe/interface implemente/hérite d'une interface du niveau N-1
			if (clazz.getSuperclass() != null && subLevelClasses.contains(clazz.getSuperclass())
					|| containsOne(subLevelClasses, clazz.getInterfaces())) {
				actuelLevelClasses.add(clazz);
				remainingClasses.remove(clazz);
			}
		}
		ordredClasses.put(level, actuelLevelClasses);
		ordredClasses.putAll(getClassesLevels(level + 1, remainingClasses, actuelLevelClasses));

		return ordredClasses;
	}

	/*******************************/
	// Return true if the Set contains one at least one element of the array
	public static <T> boolean containsOne(Set<T> source, T[] elements) {
		for (T t : elements) {
			if (source.contains(t)) {
				return true;
			}
		}
		return false;
	}
}