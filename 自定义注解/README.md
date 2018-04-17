#### 注解语法
* @interface 合起来算一个关键字
* 访问符 可以是public package 
* 不能继承其他接口或者类型
* 配置属性： 用类似方法的格式写  配置属性类型是返回值  用default关键字定义默认值 (有默认值的注解是可选元素)
* 配置属性： 基本类型、Class、String、enum、Annotation类型 以及以上数组
* 配置属性： 注解元素必须有确定的值，要么在定义注解的默认值中指定，要么在使用注解时指定，非基本类型的注解元素的值不可为null
* 配置属性： 在定义注解时，使用特殊值，例如空字符串("")或者负数(-1)，表示某个元素不存在，使处理器分辨出一个元素的存在或缺失的状态
* Java使用Annotation接口来代表 类属性 前面的注解
* 当一个Annotation类型被定义为运行时的Annotation后，该注解才能是运行时可见，当class文件被装载时被保存在class文件中的Annotation才会被虚拟机读取  @Retention(RetentionPolicy.RUNTIME)
* 如果注解没有定义为运行时可见， 运行时 ，类属性前面的对应注解，就相当于删除了

#### Annotation分类
* 标记注解:一个没有成员定义的Annotation类型被称为标记注解。这种Annotation类型仅使用自身的存在与否来为我们提供信息。比如后面的系统注解@Override;
* 单值注解
* 完整注解　　

#### Java注解三种目的
* 构建时指示: RetentionPolicy.SOURCE   只存在于源码中，不会存在于.class文件中，在编译时会被忽略掉
* 编译期指示: RetentionPolicy.CLASS    只存在于.class文件中，在编译期有效，但是在运行期会被忽略掉，这也是默认范围
* 运行时指示: RetentionPolicy.RUNTIME  在运行期有效，JVM在运行期通过反射获得注解信息

### 注解处理器
* 如果没有用来读取注解的方法和工作，那么注解也就不会比注释更有用处了
* Java SE5扩展了反射机制的API，以帮助程序员快速的构造自定义注解处理器
* ‘注解处理器’类库(java.lang.reflect.AnnotatedElement)
* Java使用Annotation接口来代表‘程序元素’(Class/Constructor/Field/Method/Package) 前面的注解,该接口是所有Annotation类型的父接口 (e.g Field.getAnnotations()返回的是  Annotation实例数组 )
* Java在java.lang.reflect 包下新增了AnnotatedElement接口，该接口代表程序中可以接受注解的‘程序元素’，也就是任何‘程序元素’只要实现这个接口，就可以接受注解
* 当前实现了 AnnotatedElement接口的程序元素 有 Class/Constructor/Field/Method/Package 这些类都实现了AnnotatedElement接口
* 尽管一些annotation通过java的反射api方法在运行时被访问，而java语言解释器在工作时忽略了这些annotation。正是由于java虚拟机忽略了Annotation，导致了annotation类型在代码中是“不起作用”的； 只有通过“某种配套的工具”(对应的注解处理器??)才会对annotation类型中的信息进行访问和处理


#### API
* Field 提供如下方法 (因为Field实现了AnnotatedElement接口 ) 找到属性上的注解 和 注解配置的值 (同样适用于Class/Method)
* <T extends Annotation> T getAnnotation( Class<T> annotationClass) 获取该Field上annotationClass类型的注解 没有返回null
* Annotation[] getAnnotations() 返回该Field上的所有注解(Annotation实例数组)
* boolean isAnnotationPresent(Class<?extends Annotation> annotationClass)  判断该Field上是否包含指定类型的注解，存在则返回true


#### 系统内置标准注解
* @Override：用于修饰此方法覆盖了父类的方法;
* @Deprecated：用于修饰已经过时的方法;
* @SuppressWarnnings:用于通知java编译器禁止特定的编译警告
* Java编译器 会有 注解处理器 ，找到类上的属性或者方法，有这些注解的，然后执行相关处理(自己觉得)

#### 两个元注解：@Retention和@Target
* 元注解就是注解的注解
* @Retention用来定义当前注解的作用范围
* @Retention(RetentionPolicy.RUNTIME) 限制为运行时有效， 这个注解会告诉编译器和JVM，这个注解需要在运行时有效，JVM会在运行时通过反射机制获取注解信息
* @Target注解用来约束自定义注解可以注解Java的哪些元素
* @Target({ElementType.METHOD}) 只能注解类的方法
* @Inherited注解 ，被这种注解了的类，的派生类，也继承这个注解 
* @Documented 告知JavaDoc工具，当前注解本身也要显示在Java Doc中

