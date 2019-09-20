# LimpezaArquivosNoPrazo

Esta Api apaga arquivos e pastas em um prazo específico passado por parâmetro!!! 

Lista Arquivos em um intevalo de Tempo excluindo tudo dentro do prazo. 

"PRECISA SEGUIR A ESTRUTURA DE PASTAS ANO-->MES-->DIA-->HORA"  

*** a Classe CalendárioUtils lista Ano,Mes,Dia e Hora ********  

*** a Classe Limpeza Tem a Função IniciarExclusao que pede o 
Diretório Raiz dataInicial e dataFinal para Calcular o intervalo entre o 
Inicio e Fim e a Hora termino para interromper e finalizar a tarefa caso passe daquele horario. 
Após receber os parametros verifica se o prazo está dentro do mesmo ano e chama a respectiva
função de exclusão para entrar no(s) ano(s) encontrado(s),listar os Meses disponíveis e entrar 
nos meses dentro do prazo listar e excluir Dias dentro do prazo e entrar nos Dias Inicial e 
final e fazer as exlusões de Horas dentro deste prazo ************
