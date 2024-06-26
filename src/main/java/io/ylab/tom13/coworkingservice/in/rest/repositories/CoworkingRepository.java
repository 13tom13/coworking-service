package io.ylab.tom13.coworkingservice.in.rest.repositories;


import io.ylab.tom13.coworkingservice.in.entity.model.coworking.Coworking;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingConflictException;
import io.ylab.tom13.coworkingservice.in.exceptions.coworking.CoworkingNotFoundException;
import io.ylab.tom13.coworkingservice.in.exceptions.repository.RepositoryException;

import java.util.Collection;
import java.util.Optional;

public interface CoworkingRepository {

    Collection<Coworking> getAllCoworking();

    Optional<Coworking> getCoworkingById(long coworkingId) throws CoworkingNotFoundException;

    Optional<Coworking> createCoworking(Coworking coworkingDTO) throws CoworkingConflictException, RepositoryException;

    void deleteCoworking(long coworkingId) throws CoworkingNotFoundException;

    Optional<Coworking> updateCoworking(Coworking coworkingDTO) throws CoworkingNotFoundException, CoworkingConflictException;
}
