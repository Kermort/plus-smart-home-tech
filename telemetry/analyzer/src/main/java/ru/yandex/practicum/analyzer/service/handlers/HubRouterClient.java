package ru.yandex.practicum.analyzer.service.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubRouterClient {
    @GrpcClient("hub-router")
    HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouter;

    public void sendRequest(DeviceActionRequest actionRequest) {
        try {
            log.info("[Hub router client] отправка grpc {}", actionRequest);
            hubRouter.handleDeviceAction(actionRequest);
        } catch (Exception e) {
            log.error("Ошибка при отправке сообщения grpc", e);
        }
    }


}
