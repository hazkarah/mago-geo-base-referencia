# Contribuição

Guia para contribuir para o projeto.

_Issues_ ou _pull requests_ que não estejam de acordo com este guia serão recusados.

## Linguagem

- _Commits_ devem ser feitos em inglês.
- _Issues_, _pull requests_ e revisões podem ser feitas em português para facilitar a comunicação e chegar a um consenso rapidamente.

## Processo de Pull Request

1. Certifique-se de que quaisquer dependências de instalação ou construção sejam removidas antes do fim da camada ao fazer uma
build.
2. Atualize o README.md com detalhes das alterações na interface, isso inclui novo ambiente variáveis, portas expostas, locais úteis de arquivos e parâmetros de contêiner.
3. Aumente os números de versão em todos os arquivos de exemplos e a README.md para a nova versão que este pull request. O esquema de versionamento que usamos é [SemVer](http://semver.org/).
4. Você pode fazer o merge request uma vez que você tenha a aprovação de dois outros desenvolvedores, ou se você
não tem permissão para fazer isso, você pode solicitar ao segundo revisor para o merge para você.
5. Sempre trabalhe com gitflow para qualquer alteração. A estruutura do gitflow é:
    * master
    * develop
    * feature
    * release
    * hotfix

6. Informe sempre o redmine que está em atendimento. Utilizando o gitflow, um repositório de exemplo seria:
    * feature/344-manutencao-cadastro
    * release/434-aplicacao-exemplo
    * hotfix/3343-correcao-critica-producao 

## Desenvolvimento

- Desenvolva apenas em seu _branch_.
- Faça _rebase_ com o **master** **antes** de criar _pull requests_.
- Faça _rebase_ do **master** em seu _branch_ diariamente.
- Faça squash dos seus commits que não estão completos (_Work In Progress_).

### O que **não** fazer
- Desenvolver direto no **master**.
- _Merge_ de _branches_ no **master** antes que o código seja revisado.
- _Merge_ de _branches_ no **master** antes que todas as discussões tenham se encerrado.
- _Merge_ de _pull requests_ que tenham _reviews_ pendentes (_requested_ ou ainda não finalizadas)

## _Commits_

- Faça _commits_ pequenos, que descrevem alterações únicas.
- Siga as referências sobre como escrever mensagens de _commits_:
    - [_Commit Guidelines_](https://git-scm.com/book/en/v2/Distributed-Git-Contributing-to-a-Project#_commit_guidelines)
    - [_How to Write a Git Commit Message_](https://chris.beams.io/posts/git-commit/)
- Seja breve e suscinto no título das mensagens do _commit_.
- Use o corpo da mensagem para explicações adicionais.
- Se esforce para manter o **master** o mais limpo possível.

## Issues

Use _issues_ para discutir problemas e ideias para o projeto..