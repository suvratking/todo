package com.suvrat.todo.pojo;

import com.suvrat.todo.entity.UserEntity;

import java.util.Set;

public record TodoResponse(Long id, String title, boolean completed, Set<UserEntity> users) {
}
