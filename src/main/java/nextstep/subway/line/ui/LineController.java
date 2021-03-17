package nextstep.subway.line.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class LineController {
    private final LineService lineService;

    public LineController(final LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping
    public ResponseEntity createLine(@AuthenticationPrincipal LoginMember loginMember, @RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(loginMember, lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @GetMapping
    public ResponseEntity<List<LineResponse>> findAllLines(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok(lineService.findLineResponses(loginMember));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineResponse> findLineById(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
        return ResponseEntity.ok(lineService.findLineResponseById(loginMember, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateLine(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id, @RequestBody LineRequest lineUpdateRequest) {
        lineService.updateLine(loginMember, id, lineUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteLine(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
        lineService.deleteLineById(loginMember, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{lineId}/sections")
    public ResponseEntity addLineStation(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        lineService.addSection(loginMember, lineId, sectionRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{lineId}/sections")
    public ResponseEntity removeLineStation(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long lineId, @RequestParam Long stationId) {
        lineService.removeSection(loginMember, lineId, stationId);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }
}