package com.website.loveconnect.repository.custom;

import jakarta.persistence.Tuple;

public interface UserRepositoryCustom {
    Tuple getUserById(Integer idUser);
    Tuple getUserForUpdateById(Integer idUser);
}
