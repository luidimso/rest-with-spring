package com.luidimso.mapper;

import java.util.ArrayList;
import java.util.List;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

public class DozerMapperr {
	private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();
	
	// For the newest versions, the dependency ModelMapper can be used instead of DozerMapper
	
	public static <O, D> D parseObjact(O origin, Class<D> destination) {
		return mapper.map(origin, destination);
	}
	
	public static <O, D> List<D> parseListObjacts(List<O> origin, Class<D> destination) {
		List<D> destinationObjects = new ArrayList<D>();
		
		for(O o : origin) {
			destinationObjects.add(mapper.map(o, destination));
		}
		
		return destinationObjects;
	}
}
