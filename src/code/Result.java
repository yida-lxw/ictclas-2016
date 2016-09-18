package code;

/**
 * Created by Lanxiaowei
 */
public class Result {
    /**Token文本*/
    private String text;
    /**词性*/
    private String type;
    /**当前第几个Token*/
    private int position;
    /**起始位置*/
    private int start;
    /**Token文本长度*/
    private int length;

    public Result() {}

    public Result(String text, String type, int position, int start, int length) {
        this.text = text;
        this.type = type;
        this.position = position;
        this.start = start;
        this.length = length;
    }

    @Override
    public String toString() {
        int maxlen = 5;
        String pos = position + "";
        int time = maxlen - pos.length();
        if(time <= 0) {
            pos = position + "";
        } else {
            for(int i=0; i < time; i++) {
                pos = " " + pos;
            }
        }
        return pos + ":[" + text + "]-{" + type + "}" + "\t" +
                "[" + start + "," + (start + length - 1) + "]";
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
