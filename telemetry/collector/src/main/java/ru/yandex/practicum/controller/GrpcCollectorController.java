package ru.yandex.practicum.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.service.CollectorService;

@Slf4j
@GrpcService
public class GrpcCollectorController extends CollectorControllerGrpc.CollectorControllerImplBase {
    private final CollectorService collectorService;

    public GrpcCollectorController(CollectorService collectorService) {
        this.collectorService = collectorService;
    }

    @Override
    public void collectSensorEvent(SensorEventProto event, StreamObserver<Empty> streamObserver) {
        log.info("[Grpc collector controller] sensor event (type={})", event.getPayloadCase());
        try {
            collectorService.collectSensorEvent(event);
            streamObserver.onNext(Empty.getDefaultInstance());
            streamObserver.onCompleted();
        } catch (Exception e) {
            streamObserver.onError(new StatusRuntimeException(Status.fromThrowable(e)));
        }
    }

    @Override
    public void collectHubEvent(HubEventProto event, StreamObserver<Empty> streamObserver) {
        log.info("[Grpc collector controller] hub event (type={})", event.getPayloadCase());
        try {
            collectorService.collectHubEvent(event);
            streamObserver.onNext(Empty.getDefaultInstance());
            streamObserver.onCompleted();
        } catch (Exception e) {
            streamObserver.onError(new StatusRuntimeException(Status.fromThrowable(e)));
        }
    }

}
