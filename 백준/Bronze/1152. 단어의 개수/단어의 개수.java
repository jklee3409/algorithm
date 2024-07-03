import java.io.IOException;

public class Main{
    public static void main(String[] args) throws IOException {
        int count = 0;
        int pre_str = ' ';
        int str = 0;

        while(true){
            str = System.in.read();

            if(str == ' '){
                if(pre_str != ' ') count++;
            } else if (str == '\n') {
                if(pre_str != ' ') count++;
                break;
            }

            pre_str = str;
        }

        System.out.println(count);
    }
}