package com.ecom.service_impl;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ecom.IService.CommonService;

import jakarta.servlet.http.HttpSession;

@Service
public class CommonServiceImpl implements CommonService
{

	@Override
	public void removeMessage() 
	{
		try
		{
			System.out.println("SessionHelper.removeMessage()");
			HttpSession session=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
			session.removeAttribute("succMsg");
			session.removeAttribute("errorMsg");
		}
		catch(Exception e)
		{
			System.out.println("Error In Session Helper...");
			e.printStackTrace();
		}

	}

}
