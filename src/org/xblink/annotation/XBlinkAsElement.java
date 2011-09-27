package org.xblink.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该字段作为元素进行序列化。<br>
 * 
 * 例如：
 * 
 * <pre>
 * public class User {
 * 
 * 	&#064;XBlinkAsElement
 * 	private String name;
 * 
 * 	// 其他方法省略...
 * }
 * </pre>
 * 
 * 生成结果为:
 * 
 * <pre>
 * <code>
 * <User><name>你设定的值</name></User>
 * </code>
 * </pre>
 * 
 * 
 * @author pangwu86(pangwu86@gmail.com)
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface XBlinkAsElement {
}
