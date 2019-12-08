import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: sunxianpeng
 * \* Date: 2019/9/9
 * \* Time: 16:39
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class App {
    public static void main(String[] args) {
        List<Bean> listData= new ArrayList<>();
        Bean  b1 = new Bean();
        b1.setPlatform_id(1);
        b1.setGift_id_value(1.1);
        Bean  b2 = new Bean();
        b2.setPlatform_id(2);
        b2.setGift_id_value(2.1);
        Bean  b3 = new Bean();
        b3.setPlatform_id(3);
        b3.setGift_id_value(3.1);
        Bean  b4 = new Bean();
        b4.setPlatform_id(4);
        b4.setGift_id_value(4.1);

        listData.add(b3);
        listData.add(b1);
        listData.add(b2);
        listData.add(b4);

        listData.sort(Comparator.comparing(Bean::getGift_id_value).reversed());


        for (Bean bean :listData){
            System.out.println(bean.toString());
        }
    }
}