# Prática Offline 3 - Monitoramento de servidores

Este projeto se trata de uma prática offline da disciplina de Programação Concorrente e Distribuída.

## Definições

Se trata de uma prática similar às que realizamos no decorrer da disciplina, com ênfase em sistemas *publisher*/*subscriber*. Neste trabalho, trataremos de simular um sistema de monitoramento de serviços críticos em um cluster com três servidores. 

O sistema envia mensagens por diferentes filas de acordo com o estado do serviço, emitindo ordens de serviço de manutenção. A classificação segue o padrão: 
- Vermelho para crítico
- Amarelo para *warning*
- Azul para normal

## Detalhes do funcionamento

Teremos dois serviços críticos, definidos nesse trabalho como banco de dados e um servidor web, ambos distribuídos no cluster de três servidores. A ideia é monitorar o status dos serviços e, se algum deles entrar em status de *warning* ou crítico, emitir o chamado para a manutenção.

### Publisher

Servidores no cluster possuem um agente de monitoramento que, através de certas métricas, infere o estado do serviço e publica as informações nos determinados tópicos através de um *broker* de mensageria (RabbitMQ).

- Servidor 1 publica nos tópicos "server1.service1" e "server1.service2"
- Servidor 2 publica nos tópicos "server2.service1" e "server2.service2"
- Servidor 3 publica nos tópicos "server3.service1" e "server3.service2"

Usaremos as seguintes métricas:

- Uso de CPU 
- Uso de memória
- Tempo de resposta
- Número de conexões ativas

A saída do tópico pode ser algo como:


```
{
    "timestamp": "2024-10-04T10:00:00Z",
    "service": "Service 1",
    "status": "warning",
    "server": "Server 1",
    "metrics": {
        "cpu_usage": 85,
        "memory_usage": 70,
        "response_time": 300
    }
}
```

### Subscriber

É um serviço de monitoramento centralizado que é assinante dos dois serviços. Este atualiza o *status* dos serviços de acordo com os dados que vem dos tópicos, emitindo mensagens quando os serviços mudam seus *status*. Também pode gerar relatórios de históricos de incidentes.

### Fila de mensagens

Recebe as mensagens do serviço de monitoramento quando o status de algum serviço mudar para *warning* ou *critical*, com detalhes sobre o serviço e o problema em questão. 

A ordem de serviço pode ser algo como:

```
{
    "timestamp": "2024-10-04T10:05:00Z",
    "server": "Server 2",
    "service": "Service 2",
    "status": "critical",
    "problem": "CPU usage reach 98%, service is not responding",
    "action_required": "check and restart service"
}
```

## Tecnologias usadas

- [RabbitMQ](https://www.rabbitmq.com/docs/download)