
package org.rifidi.edge.client.ale.logicalreaders;

import java.util.Collection;

import org.eclipse.core.expressions.PropertyTester;
import org.rifidi.edge.client.ale.logicalreaders.decorators.LRSpecDecorator;

/**
 * TODO: Class level comment.  
 * 
 * @author Jochen Mader - jochen@pramari.com
 */
public class LRSpecDecoratorPropertyTester extends PropertyTester {

	/**
	 * Constructor.  
	 */
	public LRSpecDecoratorPropertyTester() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.expressions.IPropertyTester#test(java.lang.Object, java.lang.String, java.lang.Object[], java.lang.Object)
	 */
	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if ("locked".equals(property)) {
			for (Object test : (Collection<?>) receiver) {
				if(test instanceof LRSpecDecorator){
					if(!((LRSpecDecorator)test).isIsComposite()){
						return false;
					}
				}
				else{
					return false;
				}
			}
		}
		return true;
	}

}
