package ita.softserve.course_evaluation.two_factor_verif;

import lombok.Data;

/**
 * @author Mykhailo Fedenko on 07.09.2021
 */
@Data
public class TotpRequest {
    private String email;
    private boolean active2fa;
}
