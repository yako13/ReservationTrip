package Goods.Reservation_Trip.util;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FileStorageService {
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) {
            return;
        }
        File file = new File(fileUrl);
        if (file.exists()) {
            file.delete();
        }
    }
}
