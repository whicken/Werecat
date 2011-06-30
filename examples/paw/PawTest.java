import com.whicken.werecat.paw.*;
import java.util.*;

public class PawTest {
    public static class Artist {
	String name;
	int age;
	public Artist(String name, int age) {
	    this.name = name;
	    this.age = age;
	}
	public String getName() { return name; }
	public int getAge() { return age; }
	public char getFirst() {
	    return name.charAt(0);
	}
    };
    public static void main(String[] args) {
	List<Artist> artists = new ArrayList<Artist>();
	artists.add(new Artist("Bob", 20));
	artists.add(new Artist("Jill", 21));

	PawParser<Artist> parser = new PawParser(Artist.class);
	// PawExpression<Artist> expr = parser.parse("age == 20");
	// PawExpression<Artist> expr = parser.parse("first == 'B'");
	PawExpression<Artist> expr = parser.parse("name =~ /o/");
	for (Artist a : artists) {
	    Object o = expr.getValue(a);
	    System.out.println(a.name+": "+o);
	}
	
    }
}
