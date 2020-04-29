package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.repositories.NotificationRepository;
import ba.unsa.etf.si.local_server.repositories.TableRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TableService {
    private final TableRepository tableRepository;

}
