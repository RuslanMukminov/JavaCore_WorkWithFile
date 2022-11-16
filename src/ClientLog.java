import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientLog {
    private List<String[]> logList = new ArrayList<>();

    public void log(int productNum, int amount) {
        logList.add(new String[]{String.valueOf(productNum), String.valueOf(amount)});
    }

    public void exportAsCSV(File txtFile) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(txtFile))) {
            writer.writeNext(new String[]{"productNum", "amount"});
            writer.writeAll(logList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
