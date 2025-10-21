package br.com.unipar.exemplo.controller

import br.com.unipar.exemplo.database.PessoaRepository
import br.com.unipar.exemplo.model.Pessoa
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

//A anotação RestController não pode usar VIEW, basicamente é para construir APIs
//A anotação Controller permite ter VIEW, para construir Web Applications
@RestController
@RequestMapping("/pessoa")
class PessoaController(
    private val pessoaRepository : PessoaRepository

){
    @PostMapping
    fun cadastrarPessoa(@RequestBody pessoa: Pessoa)
    : ResponseEntity<Pessoa>{
        return ResponseEntity.ok(
            pessoaRepository.save<Pessoa>(pessoa))
    }


    @GetMapping
    fun buscarPessoas() : ResponseEntity<List<Pessoa>>{
        return ResponseEntity.ok(pessoaRepository.findAll())
    }

    @GetMapping("/{id}")
    fun buscarId(@PathVariable id : Long) : ResponseEntity<Pessoa>{
         val pessoa = pessoaRepository.findById(id)
        return if (pessoa.isPresent){
            ResponseEntity.ok(pessoa.get())
        } else{
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun excluirPessoa(@PathVariable id : Long): ResponseEntity<Void>{
        return if (pessoaRepository.existsById(id)){
            ResponseEntity.noContent().build()
        } else{
            ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/{id}")
    fun atualizarPessoa(@PathVariable id: Long, @RequestBody novaPessoa : Pessoa)
        : ResponseEntity<Pessoa>{
            return pessoaRepository.findById(id).map { pessoa ->
                val pessoaAtualizada = pessoa.copy(
                    nome = novaPessoa.nome,
                    tipo = novaPessoa.tipo,
                    cpf = novaPessoa.cpf
                )
                ResponseEntity.ok(pessoaRepository.save(pessoaAtualizada))
            }.orElse(
                ResponseEntity.notFound().build()
            )
        }
    }