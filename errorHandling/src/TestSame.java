import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TestSame {
    public TestSame() {
        String file1Path = "out.txt"; // 第一个文件的路径
        String file2Path = "output.txt"; // 第二个文件的路径

        try {
            BufferedReader reader1 = new BufferedReader(new FileReader(file1Path));
            BufferedReader reader2 = new BufferedReader(new FileReader(file2Path));

            int lineNumber = 1;
            String line1, line2;

            while ((line1 = reader1.readLine()) != null && (line2 = reader2.readLine()) != null) {
                if (!line1.equals(line2)) {
                    System.out.println("文件不同的位置（行 " + lineNumber + "）：");
                    System.out.println("文件1： " + line1);
                    System.out.println("文件2： " + line2);
                }
                lineNumber++;
            }

            if ((line1 = reader1.readLine()) != null || (line2 = reader2.readLine()) != null) {
                System.out.println("文件长度不同，有一个文件更长。");
            } else {
                System.out.println("文件相同。");
            }

            reader1.close();
            reader2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
