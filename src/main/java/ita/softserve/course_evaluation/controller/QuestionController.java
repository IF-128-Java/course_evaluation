package ita.softserve.course_evaluation.controller;

import ita.softserve.course_evaluation.entity.Question;
import ita.softserve.course_evaluation.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<Question>> getAllQuestions(){
        final List<Question> questions = questionService.getAllQuestion();

        return questions != null && !questions.isEmpty()
                ? new ResponseEntity<>(questions, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping("/addQuestion")
    public ResponseEntity<Question> addQuestion(@RequestBody Question question){
        return new ResponseEntity<>(questionService.addQuestion(question),
                HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable("id") long questionId){
        return new ResponseEntity<>(questionService.getQuestionById(questionId), HttpStatus.OK);
//        Question question = questionService.getQuestionById(questionId);
//        return question != null
//                ? new ResponseEntity<>(question, HttpStatus.OK)
//                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("{id}")
    public ResponseEntity<Question> updateEmployee(@PathVariable("id") long id
            ,@RequestBody Question question){
        return new ResponseEntity<>(questionService.updateEmployee(question, id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") long id){
        // delete question from DB
        questionService.deleteQuestionById(id);
        return new ResponseEntity<>("Question deleted successfully!.", HttpStatus.OK);
    }

}
