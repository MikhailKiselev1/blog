package main.repositories;

import main.model.GlobalSetting;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalSettingCrudRepository extends CrudRepository<GlobalSetting, Long> {
}
