package com.scm.entities;
import jakarta.persistence.*;   // this includes Id, Entity, Table, Column, etc.
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class SocialLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String link;
    private String title;
    
  @ManyToOne
  private Contact contact;
}
