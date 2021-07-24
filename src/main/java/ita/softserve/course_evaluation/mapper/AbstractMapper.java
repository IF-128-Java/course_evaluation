package ita.softserve.course_evaluation.mapper;

import ita.softserve.course_evaluation.dto.AbstractDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public abstract class AbstractMapper<E, D extends AbstractDto> implements Mapper<E, D> {
	
	@Autowired
	ModelMapper mapper;
	
	private Class<E> entityClass;
	private Class<D> dtoClass;
	
	public AbstractMapper(Class<E> entityClass, Class<D> dtoClass) {
		this.entityClass = entityClass;
		this.dtoClass = dtoClass;
	}
	
	@Override
	public E toEntity(D dto) {
		return Objects.isNull(dto) ? null : mapper.map(dto, entityClass);
	}
	
	@Override
	public D toDto(E entity) {
		return Objects.isNull(entity) ? null : mapper.map(entity, dtoClass);
	}
	
	protected Converter<E, D> toDtoConverter() {
		return context -> {
			E source = context.getSource();
			D destination = context.getDestination();
			mapSpecificFieldsInEntity(source, destination);
			return context.getDestination();
		};
	}
	
	protected Converter<D, E> toEntityConverter() {
		return context -> {
			D source = context.getSource();
			E destination = context.getDestination();
			mapSpecificFieldsInDto(source, destination);
			return context.getDestination();
		};
	}
	
	protected abstract void mapSpecificFieldsInEntity(E source, D destination);
	
	protected abstract void mapSpecificFieldsInDto(D source, E destination);
}