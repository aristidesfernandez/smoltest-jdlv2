package co.com.ies.smol.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeviceInterfaceMapperTest {

    private DeviceInterfaceMapper deviceInterfaceMapper;

    @BeforeEach
    public void setUp() {
        deviceInterfaceMapper = new DeviceInterfaceMapperImpl();
    }
}
