import static org.junit.Assert.*;
import java.util.*;

public class Test {

	//@org.junit.Test
	public void depPresBasictest() {
		AttributeSet t1 = new AttributeSet();
		AttributeSet t2 = new AttributeSet();
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		
		t1.add(new Attribute("a"));
		t2.add(new Attribute("b"));
		
		fds.add(new FunctionalDependency(t1,new Attribute("a")));

		// tables
		// a
		// b
		// fds
		// a -> a
		assertTrue(FDChecker.checkDepPres(t1, t2, fds));
		
		
		fds.add(new FunctionalDependency(t1, new Attribute("b")));
		// tables
		// a
		// b
		// fds
		// a -> a
		// a -> b
		assertFalse(FDChecker.checkDepPres(t1, t2, fds));
	}

	@org.junit.Test
	public void losslessBasictest() {
		AttributeSet t1 = new AttributeSet();
		AttributeSet t2 = new AttributeSet();
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		
		t1.add(new Attribute("a"));
		t2.add(new Attribute("b"));
		
		// tables
		// a
		// b
		// fds
		assertFalse(FDChecker.checkLossless(t1, t2, fds));
		
		t1.add(new Attribute("b"));
		// tables
		// a b
		// b
		// fds
		assertTrue(FDChecker.checkLossless(t1, t2, fds));
	}
	
	@org.junit.Test
	public void depPresFDtest() {
		AttributeSet t1 = new AttributeSet();
		AttributeSet t2 = new AttributeSet();
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		
		t1.add(new Attribute("a"));
		t1.add(new Attribute("b"));
		t2.add(new Attribute("b"));
		t1.add(new Attribute("c"));
		
		fds.add(new FunctionalDependency(t1,new Attribute("b")));

		// tables
		// a b
		// b
		// fds
		// ab -> b
		assertTrue(FDChecker.checkDepPres(t1, t2, fds));
		
		
		fds.add(new FunctionalDependency(t2, new Attribute("a")));
		// tables
		// a b
		// b
		// fds
		// ab -> b
		// b -> a
		assertTrue(FDChecker.checkDepPres(t1, t2, fds));
		
		fds.add(new FunctionalDependency(t2, new Attribute("c")));
		assertTrue(FDChecker.checkDepPres(t1, t2, fds));
		
		AttributeSet fd= new AttributeSet();
		fd.add(new Attribute("a"));
		fd.add(new Attribute("b"));
		fds.add(new FunctionalDependency(fd, new Attribute("c")));
		assertTrue(FDChecker.checkDepPres(t1, t2, fds));
		
		t2.add(new Attribute("d"));
		
		AttributeSet fd3= new AttributeSet();
		fd3.add(new Attribute("a"));
		fd3.add(new Attribute("b"));
		fds.add(new FunctionalDependency(fd3, new Attribute("d")));
		assertFalse(FDChecker.checkDepPres(t1, t2, fds));
		
		fds.remove(fd3);
		
		AttributeSet fd2= new AttributeSet();
		fd2.add(new Attribute("a"));
		fds.add(new FunctionalDependency(fd2, new Attribute("d")));
		assertFalse(FDChecker.checkDepPres(t1, t2, fds));
		
	}
	
	@org.junit.Test
	public void closureTest() {
		// remember to change closure() back to private function
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		AttributeSet fd= new AttributeSet();
		fd.add(new Attribute("a"));
		fd.add(new Attribute("b"));
		fds.add(new FunctionalDependency(fd, new Attribute("c")));
		
		AttributeSet fd1= new AttributeSet();
		fd1.add(new Attribute("c"));
		fds.add(new FunctionalDependency(fd1, new Attribute("d")));
		
		AttributeSet fd2= new AttributeSet();
		fd2.add(new Attribute("a"));
		fd2.add(new Attribute("d"));
		fds.add(new FunctionalDependency(fd2, new Attribute("e")));
		
		AttributeSet attrs= new AttributeSet();
		attrs.add(new Attribute("a"));
		attrs.add(new Attribute("c"));
		
		// fds
		// ab -> c
		// c -> d
		// ad -> e
		
		AttributeSet closure= new AttributeSet();
		closure.add(new Attribute("a"));
		closure.add(new Attribute("e"));
		closure.add(new Attribute("c"));
		closure.add(new Attribute("d"));
		assertEquals(FDChecker.closure(attrs, fds), closure);
		
	}
	
	@org.junit.Test
	public void losslesstest() {
		AttributeSet t1 = new AttributeSet();
		AttributeSet t2 = new AttributeSet();
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		
		t1.add(new Attribute("a"));
		t1.add(new Attribute("b"));
		t2.add(new Attribute("b"));
		t2.add(new Attribute("c"));
		t2.add(new Attribute("d"));
		
		AttributeSet temp = new AttributeSet();
		temp.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp,new Attribute("c")));
		// tables
		// a b
		// b c d
		// fds
		// b -> c
		assertFalse(FDChecker.checkLossless(t1, t2, fds));
		
		fds.add(new FunctionalDependency(temp, new Attribute("d")));
		// tables
		// a b
		// b c d
		// fds
		// b -> c
		// b -> d
		assertTrue(FDChecker.checkLossless(t1, t2, fds));
	}
}
