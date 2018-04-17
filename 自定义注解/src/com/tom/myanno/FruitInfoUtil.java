package com.tom.myanno;

import java.lang.reflect.Field;

/*  注解处理器 
 *   
 *   如果没有用来读取注解的方法和工作，那么‘注解’也就不会比‘注释’更有用处了。
 *   
 *   使用注解的过程中，很重要的一部分就是创建于使用注解处理器。
 *   
 *   Java SE5扩展了反射机制的API，以帮助程序员快速的构造自定义注解处理器。
 * 
 * 
 * */

public class FruitInfoUtil {
	
	public static void getFruitInfo(Class<?> clazz){
	        
	        String strFruitName=" 水果名称：";
	        String strFruitColor=" 水果颜色：";
	        String strFruitProvicer="供应商信息：";
	        
	        Field[] fields = clazz.getDeclaredFields();
	        
	        for(Field field :fields){
	        	// 判断当前类的 所有属性上  有哪个注解类型
	            if(field.isAnnotationPresent(FruitName.class)){
	                FruitName fruitName = (FruitName) field.getAnnotation(FruitName.class);
	                strFruitName=strFruitName+fruitName.value();
	                System.out.println(strFruitName);
	            }
	            else if(field.isAnnotationPresent(FruitColor.class)){
	                FruitColor fruitColor= (FruitColor) field.getAnnotation(FruitColor.class);
	                strFruitColor=strFruitColor+fruitColor.fruitColor().toString();
	                System.out.println(strFruitColor);
	            }
	            else if(field.isAnnotationPresent(FruitProvider.class)){
	                FruitProvider fruitProvider= (FruitProvider) field.getAnnotation(FruitProvider.class);
	                strFruitProvicer=" 供应商编号："+fruitProvider.id()+" 供应商名称："+fruitProvider.name()+" 供应商地址："+fruitProvider.address();
	                System.out.println(strFruitProvicer);
	            }
	        }
	    }
}
