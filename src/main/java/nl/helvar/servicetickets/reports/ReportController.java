package nl.helvar.servicetickets.reports;

import nl.helvar.servicetickets.exceptions.RecordNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    private ReportRepository reportRepository;

    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportRepository.findAll();

        if (reports.isEmpty()) {
            throw new RecordNotFoundException("Could not find any reports in database.");
        } else {
            return new ResponseEntity<>(reports, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> findReportById(@PathVariable("id") Long id) {
        Optional<Report> Report = reportRepository.findById(id);

        if (Report.isEmpty()) {
            throw new RecordNotFoundException("Could not find any report with id '" + id + "' in database.");
        } else {
            return new ResponseEntity<>(Report.get(), HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<Report> addReport(@RequestBody Report Report) {
        reportRepository.save(Report);

        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/" + Report.getId())
                        .toUriString()
        );

        return ResponseEntity.created(uri).body(Report);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Report> replaceReport(@PathVariable("id") Long id, @RequestBody Report newReport) {
        Optional<Report> Report = reportRepository.findById(id);

        if (Report.isEmpty()) {
            throw new RecordNotFoundException("Could not find any report with id '" + id + "' in database.");
        } else {
            Report existingReport = Report.get();

            BeanUtils.copyProperties(newReport, existingReport, "id");

            reportRepository.save(existingReport);

            return new ResponseEntity<>(existingReport, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Report> deleteReport(@PathVariable("id") Long id) {
        Optional<Report> Report = reportRepository.findById(id);

        if (Report.isEmpty()) {
            throw new RecordNotFoundException("Could not find any report with id '" + id + "' in database.");
        } else {
            Report existingReport = Report.get();

            reportRepository.delete(existingReport);

            return new ResponseEntity<>(existingReport, HttpStatus.OK);
        }
    }
}
