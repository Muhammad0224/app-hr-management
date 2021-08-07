package uz.pdp.apphrmanagement.service;


import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagement.entity.Employee;
import uz.pdp.apphrmanagement.entity.TourniquetCard;
import uz.pdp.apphrmanagement.entity.TourniquetHistory;
import uz.pdp.apphrmanagement.model.ApiResponse;
import uz.pdp.apphrmanagement.model.TourniquetCardDto;
import uz.pdp.apphrmanagement.model.TourniquetHistoryDto;
import uz.pdp.apphrmanagement.repository.EmployeeRepository;
import uz.pdp.apphrmanagement.repository.TourniquetHistoryRepository;
import uz.pdp.apphrmanagement.repository.TourniquetRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class TourniquetService {
    private final EmployeeRepository employeeRepository;
    private final TourniquetRepository tourniquetRepository;
    private final TourniquetHistoryRepository tourniquetHistoryRepository;

    public TourniquetService(EmployeeRepository employeeRepository, TourniquetRepository tourniquetRepository, TourniquetHistoryRepository tourniquetHistoryRepository) {
        this.employeeRepository = employeeRepository;
        this.tourniquetRepository = tourniquetRepository;
        this.tourniquetHistoryRepository = tourniquetHistoryRepository;
    }

    public ApiResponse create(TourniquetCardDto dto) {
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(dto.getEmployeeEmail());
        if (!optionalEmployee.isPresent())
            return new ApiResponse("Employee not found", false);

        Employee employee = optionalEmployee.get();

        TourniquetCard card = new TourniquetCard();
        card.setCompany(employee.getCompany());
        card.setEmployee(employee);
        tourniquetRepository.save(card);
        return new ApiResponse("Tourniquet card is successfully created", true);
    }

    public ApiResponse edit(TourniquetCardDto dto, String email) {
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(dto.getEmployeeEmail());
        if (!optionalEmployee.isPresent())
            return new ApiResponse("Employee not found", false);

        Optional<TourniquetCard> optionalTourniquetCard = tourniquetRepository.findByEmployee_EmailAndStatusTrue(email);
        if (optionalTourniquetCard.isPresent()) {
            TourniquetCard card = optionalTourniquetCard.get();
            card.setEmployee(optionalEmployee.get());
            tourniquetRepository.save(card);
            return new ApiResponse("Card updated", true);
        }
        return new ApiResponse("Card not found", false);
    }

    public ApiResponse enter(TourniquetHistoryDto dto) {
        Optional<TourniquetCard> cardOptional = tourniquetRepository.findById(UUID.fromString(dto.getCardId()));
        if (!cardOptional.isPresent())
            return new ApiResponse("Card not found", false);
        TourniquetCard card = tourniquetRepository.save(checkActive(cardOptional.get()));
        if (card.isStatus()) {
            TourniquetHistory tourniquetHistory = new TourniquetHistory();
            tourniquetHistory.setEnteredAt(Timestamp.valueOf(LocalDateTime.now()));
            tourniquetHistory.setTourniquetCard(card);
            tourniquetHistoryRepository.save(tourniquetHistory);
            return new ApiResponse("Entered", true);
        }
        return new ApiResponse("Expiration date of the card", false);
    }

    public ApiResponse exit(TourniquetHistoryDto dto) {
        Optional<TourniquetCard> cardOptional = tourniquetRepository.findById(UUID.fromString(dto.getCardId()));
        if (!cardOptional.isPresent())
            return new ApiResponse("Card not found", false);
        TourniquetCard card = tourniquetRepository.save(checkActive(cardOptional.get()));
        if (card.isStatus()) {
            TourniquetHistory tourniquetHistory = new TourniquetHistory();
            tourniquetHistory.setExitedAt(Timestamp.valueOf(LocalDateTime.now()));
            tourniquetHistory.setTourniquetCard(card);
            tourniquetHistoryRepository.save(tourniquetHistory);
            return new ApiResponse("Exited", true);
        }
        return new ApiResponse("Expiration date of the card", false);
    }

    public ApiResponse activate(TourniquetHistoryDto dto) {
        Optional<TourniquetCard> optionalTourniquetCard =
                tourniquetRepository.findById(UUID.fromString(dto.getCardId()));
        if (optionalTourniquetCard.isPresent()) {
            TourniquetCard card = optionalTourniquetCard.get();
            card.setStatus(true);
            card.setExpireDate(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365 * 3));
            tourniquetRepository.save(card);
            return new ApiResponse("Card activated", true);
        }
        return new ApiResponse("Card not found", true);
    }

    public TourniquetCard checkActive(TourniquetCard card) {
        if (card.getExpireDate().before(new Date())) {
            card.setStatus(false);
        }
        return card;
    }

    public ApiResponse delete(String id) {
        Optional<TourniquetCard> optionalTourniquetCard =
                tourniquetRepository.findById(UUID.fromString(id));
        if (!optionalTourniquetCard.isPresent())
            return new ApiResponse("Card not found", true);
        TourniquetCard card = optionalTourniquetCard.get();
        card.setStatus(false);
        tourniquetRepository.save(card);
        return new ApiResponse("Card deleted",true);
    }
}
