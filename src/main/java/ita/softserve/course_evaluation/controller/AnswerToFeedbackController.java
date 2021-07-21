package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.dto.AnswerDto;
import ita.softserve.course_evaluation.service.AnswerToFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/answers")
public class AnswerToFeedbackController {

    private final AnswerToFeedbackService answerService;

    @Autowired
    public AnswerToFeedbackController(AnswerToFeedbackService answerService) {
        this.answerService = answerService;
    }

    @GetMapping()
    public ResponseEntity<List<AnswerDto>> getAllAnswers(){
        final List<AnswerDto> answerList = answerService.getAllAnswer();
        return answerList != null && !answerList.isEmpty()
                ? new ResponseEntity<>(answerList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        return new ResponseEntity<>(answerList, HttpStatus.OK);
    }

    @PostMapping("/addAnswer")
    public ResponseEntity<AnswerDto> addAnswer(AnswerDto answerDto) {
        return new ResponseEntity<>(answerService.saveAnswer(answerDto), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<AnswerDto> getAnswerById(@PathVariable("id") long id){
        return new ResponseEntity<>(answerService.findAnswerById(id),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteAnswer (@PathVariable("id") long id){
        answerService.deleteAnswerById(id);
        return new ResponseEntity<>("Answer deleted successfully.",HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<AnswerDto> updateAnswer(@RequestBody AnswerDto dto, @PathVariable("id") long id){
        return new ResponseEntity<>(answerService.updateAnswer(dto, id),HttpStatus.OK);
    }
}
