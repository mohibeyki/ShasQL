package bloodSeekers.ShasQL.BPTree;

import java.util.*;


public class EInnerNode extends ENode {

	public EInnerNode(ArrayList<Character> sector, int size) {
		super(sector, size);
	}
	public int binarySearch(short v)
	{
		int s = 0;
		int e = size-2;
		while (e - s > 2)
		{
			int m = ((e/4 + s/4) / 2);
			short tmp = (short) (sector.get(m+2)*256 + sector.get(m+3));
			if (tmp <= v)
				s = m;
			else
				e = m;
		} 
		if( e == 0)
			return 0;
		if( (short) (sector.get(s+2)*256 + sector.get(s+3))  <= v )
			return e;
		else
			return s;
	}
}
