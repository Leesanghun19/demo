package SenierProject.demo.Service;

import SenierProject.demo.domain.Food;
import SenierProject.demo.domain.Photo;
import SenierProject.demo.domain.Review;
import SenierProject.demo.domain.Store;
import SenierProject.demo.file.FileStore;
import SenierProject.demo.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PhotoService {

    private final FileStore fileStore;
    @Value("${file.dir}")
    private String fileDir;
    @Autowired
    PhotoRepository photoRepository;

    @Transactional
    public Long joinr(MultipartFile multipartFile, Review review) throws IOException {
        Photo photo = fileStore.storeFile(multipartFile);
        photo.setReview(review);
        photoRepository.save(photo);
        return photo.getId();
    }
    @Transactional
    public Long joinf(MultipartFile multipartFile, Food food) throws IOException {
        Photo photo = fileStore.storeFile(multipartFile);
        photo.setFood(food);
        photoRepository.save(photo);
        return photo.getId();
    }
    @Transactional
    public Long joins(MultipartFile multipartFile, Store store) throws IOException {
        Photo photo = fileStore.storeFile(multipartFile);
        photo.setStore(store);
        photoRepository.save(photo);
        return photo.getId();
    }
    @Transactional
    public void deletePhoto(Photo photo){
        File file = new File(fileDir+photo.getStoreFileName());

        if(file.exists()){

            file.delete();
            photoRepository.delete(photo);
        }

    }
    public String findPhoto(Long id){
        Photo photo = photoRepository.findById(id).get();
        return fileDir+photo.getStoreFileName();
    }
    @Transactional
    public void setMain(Long id){
        Photo photo = photoRepository.findById(id).get();
        photo.setMain();
    }


}
