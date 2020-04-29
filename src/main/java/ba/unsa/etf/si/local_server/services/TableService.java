package ba.unsa.etf.si.local_server.services;

import ba.unsa.etf.si.local_server.models.Notification;
import ba.unsa.etf.si.local_server.models.Table;
import ba.unsa.etf.si.local_server.repositories.NotificationRepository;
import ba.unsa.etf.si.local_server.repositories.TableRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TableService {
    private final TableRepository tableRepository;

    public List<Table> getTables() {
        return new ArrayList<>(tableRepository
                .findAll());
    }
}
