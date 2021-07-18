package ita.softserve.course_evaluation.mapper;

import ita.softserve.course_evaluation.dto.AbstractDto;

public interface Mapper<E, D extends AbstractDto> {
	
	E toEntity(D dto);
	
	D toDto(E entity);
}
