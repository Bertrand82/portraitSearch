package imageFInder.qwant;

import lombok.Lombok;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 *    params={
        'count': 50,
        'q': query,
        't': 'images',
        'safesearch': 1,
        'locale': 'en_US',
        'uiv': 4
    },
 */
@Getter
@Setter
@NoArgsConstructor

public class QuestQwant {
	
   private int count=20;
   private String q;
   private String t = "images";
   private int safesearch = 1;
   private String locale="en_US";
   private int uiv = 4;
		  
}
