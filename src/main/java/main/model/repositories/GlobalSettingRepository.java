package main.model.repositories;

import main.model.GlobalSetting;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalSettingRepository extends CrudRepository<GlobalSetting, Long> {
}
