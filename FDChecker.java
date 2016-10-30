import java.util.*;

public class FDChecker {

	/**
	 * Checks whether a decomposition of a table is dependency
	 * preserving under the set of functional dependencies fds
	 * 
	 * @param t1 one of the two tables of the decomposition
	 * @param t2 the second table of the decomposition
	 * @param fds a complete set of functional dependencies that apply to the data
	 * 
	 * @return true if the decomposition is dependency preserving, false otherwise
	 **/
	public static boolean checkDepPres(AttributeSet t1, AttributeSet t2, Set<FunctionalDependency> fds) {
		//your code here
		//a decomposition is dependency preserving, if local functional dependencies are
		//sufficient to enforce the global properties
		//To check a particular functional dependency a -> b is preserved, 
		//you can run the following algorithm
		//result = a
		//while result has not stabilized
		//	for each table in the decomposition
		//		t = result intersect table 
		//		t = closure(t) intersect table
		//		result = result union t
		//if b is contained in result, the dependency is preserved
		
		Iterator<FunctionalDependency> iterateFDS= fds.iterator();
		AttributeSet result= new AttributeSet();
		FunctionalDependency fd;
		
		while(iterateFDS.hasNext()) {
			fd= iterateFDS.next();
			result.addAll(fd.left);
			Boolean notStable= true;
			while (notStable) {
				//checking for a and its FDs in t1
				AttributeSet temp1= new AttributeSet();
				temp1.addAll(t1);
				temp1.retainAll(result);
				temp1= closure(temp1, fds);
				temp1.retainAll(t1);
				notStable= result.addAll(temp1);
				
				AttributeSet temp2= new AttributeSet();
				temp2.addAll(t2);
				temp2.retainAll(result);
				temp2= closure(temp2, fds);
				temp2.retainAll(t2);

				notStable= result.addAll(temp2) || notStable;
			}

			if (!result.contains(fd.right)) {
				return false;}
		}
		
		return true;
	}

	/**
	 * Checks whether a decomposition of a table is lossless
	 * under the set of functional dependencies fds
	 * 
	 * @param t1 one of the two tables of the decomposition
	 * @param t2 the second table of the decomposition
	 * @param fds a complete set of functional dependencies that apply to the data
	 * 
	 * @return true if the decomposition is lossless, false otherwise
	 **/
	public static boolean checkLossless(AttributeSet t1, AttributeSet t2, Set<FunctionalDependency> fds) {
		//your code here
		//Lossless decompositions do not lose information, the natural join is equal to the 
		//original table.
		//a decomposition is lossless if the common attributes for a superkey for one of the
		//tables.
		boolean lossless = false;

		AttributeSet intersect = new AttributeSet(t1);
		intersect.retainAll(t2);
		AttributeSet closure= closure(intersect, fds);
		if(closure.containsAll(t1) || closure.containsAll(t2)){
			lossless = true;
		}
		return lossless;
	}

	//recommended helper method
	//finds the total set of attributes implied by attrs
	private static AttributeSet closure(AttributeSet attrs, Set<FunctionalDependency> fds) {
		Iterator<FunctionalDependency> iterateFDS= fds.iterator();
		AttributeSet closure= new AttributeSet();
		closure.addAll(attrs);
		FunctionalDependency fd;
		Boolean unStable= true;
		while (unStable) {
			Boolean breakLoop= false;
			while (iterateFDS.hasNext()) {
				AttributeSet leftSet= new AttributeSet();
				AttributeSet leftIntersect= new AttributeSet();
				fd= iterateFDS.next();
				leftSet.addAll(fd.left);
				//left intersect = all left attributes that are in closure
				leftIntersect.addAll(fd.left);
				leftIntersect.retainAll(closure);
				
				if (leftSet.equals(leftIntersect)) {
					breakLoop= closure.add(fd.right);
					break;
				} 
			}
			unStable= breakLoop? true : false;
			if (breakLoop) {
				if (!iterateFDS.hasNext()) {
					iterateFDS= null;
					iterateFDS= fds.iterator();
					breakLoop= false;
				}
			}

		}
		return closure;
	}
}
