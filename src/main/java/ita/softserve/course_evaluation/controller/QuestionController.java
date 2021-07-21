package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.dto.QuestionDto;
import ita.softserve.course_evaluation.entity.Question;
import ita.softserve.course_evaluation.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("")
    public ResponseEntity<List<QuestionDto>> getAllQuestions(){
        final List<QuestionDto> questions = questionService.getAllQuestion();

        return questions != null && !questions.isEmpty()
                ? new ResponseEntity<>(questions, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping("/addQuestion")
    public ResponseEntity<Question> addQuestion(@RequestBody QuestionDto question){
        return new ResponseEntity<>(questionService.saveQuestion(question),
                HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<QuestionDto> getQuestionById(@PathVariable("id") long questionId){
        return new ResponseEntity<>(questionService.findQuestionById(questionId), HttpStatus.OK);
//        Question question = questionService.getQuestionById(questionId);
//        return question != null
//                ? new ResponseEntity<>(question, HttpStatus.OK)
//                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("{id}")
    public ResponseEntity<QuestionDto> updateEmployee(@PathVariable("id") long id
            ,@RequestBody QuestionDto question){
        return new ResponseEntity<>(questionService.updateQuestion(question, id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable("id") long id){
        // delete question from DB
        questionService.deleteQuestionById(id);
        return new ResponseEntity<>("Question deleted successfully!.", HttpStatus.OK);
    }

}
