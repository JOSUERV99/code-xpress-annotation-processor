package j2d.generated.service;

import dabba.doo.annotationprocessor.db.Pojo;
import j2d.generated.repository.PojoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class PojoService {
  private final PojoRepository repository;

  @Autowired
  PojoService(final PojoRepository repository) {
    this.repository = repository;
  }

  public boolean create(final Pojo instance) {
    return repository.create(instance);
  }

  public boolean delete(final Pojo instance) {
    return repository.delete(instance);
  }
}
