/**
 * Copyright 2014-2016 yangming.liu<bytefox@126.com>.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, see <http://www.gnu.org/licenses/>.
 */
package org.bytesoft.bytejta.supports.spring;

import java.lang.reflect.Proxy;

import javax.jms.XAConnectionFactory;
import javax.resource.spi.ManagedConnectionFactory;
import javax.sql.XADataSource;

import org.bytesoft.bytejta.supports.resource.ManagedConnectionFactoryHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class ManagedConnectionFactoryPostProcessor implements BeanPostProcessor {

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

		Class<?> clazz = bean.getClass();
		ClassLoader cl = clazz.getClassLoader();
		/**
		 * TODO 扫描到LocalXADataSource,创建动态代理
		 * 扫描到了我们自己创建的一个LocalXADataSource，然后针对这个LocalXADataSource，创建了一个动态代理，
		 * 动态代理的InvocationHandler是ManagedConnectionFactoryHandler，
		 * 所以,后续如果代码找LocalXADataSource的时候，其实会找到对应的这个动态代理
		 */
		Class<?>[] interfaces = clazz.getInterfaces();
		//进入到各自的动态代理类中
		if (XADataSource.class.isInstance(bean)) {
			ManagedConnectionFactoryHandler interceptor = new ManagedConnectionFactoryHandler(bean);
			interceptor.setIdentifier(beanName);
			return Proxy.newProxyInstance(cl, interfaces, interceptor);
		} else if (XAConnectionFactory.class.isInstance(bean)) {
			ManagedConnectionFactoryHandler interceptor = new ManagedConnectionFactoryHandler(bean);
			interceptor.setIdentifier(beanName);
			return Proxy.newProxyInstance(cl, interfaces, interceptor);
		} else if (ManagedConnectionFactory.class.isInstance(bean)) {
			ManagedConnectionFactoryHandler interceptor = new ManagedConnectionFactoryHandler(bean);
			interceptor.setIdentifier(beanName);
			return Proxy.newProxyInstance(cl, interfaces, interceptor);
		} else {
			return bean;
		}
	}

}
