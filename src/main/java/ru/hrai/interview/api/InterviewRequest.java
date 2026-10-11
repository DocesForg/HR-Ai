package ru.hrai.interview.api;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record InterviewRequest(@NotBlank(message="Укажите описание вакансии") @Size(max=12000,message="Описание вакансии не должно превышать 12 000 символов") String vacancyDescription, @Size(max=120,message="Название должности не должно превышать 120 символов") String position, @Size(max=80,message="Уровень не должен превышать 80 символов") String seniority, @Size(max=80,message="Фокус не должен превышать 80 символов") String questionFocus) {}
