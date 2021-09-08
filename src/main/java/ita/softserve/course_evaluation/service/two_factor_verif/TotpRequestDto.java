package ita.softserve.course_evaluation.service.two_factor_verif;

import lombok.Data;

/**
 * @author Mykhailo Fedenko
 */
@Data
public class TotpRequestDto {
    private String email;
    private boolean active2fa;
}
