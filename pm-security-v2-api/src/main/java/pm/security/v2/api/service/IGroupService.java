package pm.security.v2.api.service;

import es.common.service.ICommonMinifiedService;
import pm.security.v2.common.dto.GroupDto;
import pm.security.v2.common.dto.min.GroupMinDto;

public interface IGroupService
		extends ICommonMinifiedService<GroupDto, Long, GroupMinDto> {
	
}