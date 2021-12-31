package SenierProject.demo.Service;

import SenierProject.demo.domain.Store;
import SenierProject.demo.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    //추가
    @Transactional
    public Long join(Store store){
        storeRepository.save(store);
        return store.getId();
    }
    //삭제
    @Transactional
    public void deleteStore(Long storeId){storeRepository.deleteStore(storeId);}
    //조회
    public Store findOne(Long StoreId){return storeRepository.findOne(StoreId);}
    public Store findName(String storeName){return storeRepository.findByName(storeName);}
    public List<Store> findALl(){return storeRepository.findAll();}
}
