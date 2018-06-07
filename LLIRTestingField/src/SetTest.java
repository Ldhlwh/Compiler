import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class SetTest
{
	public static boolean isSetEqual(Set set1, Set set2)
	{
		
		if (set1 == null && set2 == null)
		{
			return true; // Both are null
		}
		
		if (set1 == null || set2 == null || set1.size() != set2.size())
		{
			return false;
		}
		
		Iterator ite1 = set1.iterator();
		Iterator ite2 = set2.iterator();
		
		boolean isFullEqual = true;
		
		while (ite2.hasNext())
		{
			if (!set1.contains(ite2.next()))
			{
				isFullEqual = false;
			}
		}
		
		return isFullEqual;
	}
	
	public static void main(String[] args)
	{
		Set<String> a = new HashSet<>();
		Set<String> b = new HashSet<>();
		
		System.out.println(isSetEqual(a, b));
	}
}
