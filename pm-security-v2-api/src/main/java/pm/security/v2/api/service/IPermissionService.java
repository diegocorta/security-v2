package pm.security.v2.api.service;

import es.common.service.ICommonMinifiedService;
import pm.security.v2.common.dto.PermissionDto;
import pm.security.v2.common.dto.min.PermissionMinDto;

public interface IPermissionService
		extends ICommonMinifiedService<PermissionDto, Long, PermissionMinDto> {
	
}