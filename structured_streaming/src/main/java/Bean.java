/**
 * \* Created with IntelliJ IDEA.
 * \* User: sunxianpeng
 * \* Date: 2019/9/9
 * \* Time: 16:41
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class Bean {
    private  int         platform_id ;
    private  String      room_id ;
    private  String        gift_id ;
    private  int         gift_id_count ;
    private  Double      gift_id_value ;

    public int getPlatform_id() {
        return platform_id;
    }

    public void setPlatform_id(int platform_id) {
        this.platform_id = platform_id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getGift_id() {
        return gift_id;
    }

    public void setGift_id(String gift_id) {
        this.gift_id = gift_id;
    }

    public int getGift_id_count() {
        return gift_id_count;
    }

    public void setGift_id_count(int gift_id_count) {
        this.gift_id_count = gift_id_count;
    }

    public Double getGift_id_value() {
        return gift_id_value;
    }

    public void setGift_id_value(Double gift_id_value) {
        this.gift_id_value = gift_id_value;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "platform_id=" + platform_id +
                ", room_id='" + room_id + '\'' +
                ", gift_id='" + gift_id + '\'' +
                ", gift_id_count=" + gift_id_count +
                ", gift_id_value=" + gift_id_value +
                '}';
    }
}
