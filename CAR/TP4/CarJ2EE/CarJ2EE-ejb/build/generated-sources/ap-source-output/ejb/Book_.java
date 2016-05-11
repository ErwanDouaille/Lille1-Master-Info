package ejb;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-05-07T15:53:46")
@StaticMetamodel(Book.class)
public class Book_ { 

    public static volatile SingularAttribute<Book, Integer> date;
    public static volatile SingularAttribute<Book, String> author;
    public static volatile SingularAttribute<Book, Float> price;
    public static volatile SingularAttribute<Book, String> title;

}