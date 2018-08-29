package com.java.web.user;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.java.web.dao.DAOInterface;
import com.java.web.util.FinalUtil;
import com.java.web.util.HttpUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class UserController {

	@Autowired
	DAOInterface di;

	// 가입하기
	@RequestMapping("/userInsert")
	public ModelAndView userInsert(HttpServletRequest req) {
		HashMap<String, Object> param = HttpUtil.getParamMap(req);
		HashMap<String, Object> map = new HashMap<String, Object>();
		System.out.println("test  __  " + param);
		param.put("sqlType", "user.userInsert");
		param.put("sql", "insert");

		int status = (int) di.call(param); // java ver1.7 이상

		if (status == 1) {
			map.put("msg", "가입이 완료 되었습니다. 로그인하세요");
			map.put("status", FinalUtil.OK);
		} else {
			map.put("msg", "가입이 되지 않았습니다");
		}
		return HttpUtil.makeJsonView(map);
	}

	// id 중복확인
	@RequestMapping("/checkId")
	public ModelAndView cId(HttpServletRequest req) {
		String userEmail = req.getParameter("checkId");
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> param = HttpUtil.getParamMap(req);
		param.put("userEmail", userEmail);
		param.put("sqlType", "user.checkId");
		param.put("sql", "selectOne");
		System.out.println("check!!!!!!!!! " + param);

		HashMap<String, Object> resultmap = (HashMap<String, Object>) di.call(param);

		int status = 0;		// 결과 상태 확인하는 값

		if (resultmap != null) {
			map.put("msg", "이미 있는 아이디입니다.");
			map.put("status", FinalUtil.NO);

		} else {
			map.put("msg", "가입 가능합니다.");
			map.put("status", FinalUtil.OK);
		}
		return HttpUtil.makeJsonView(map);
	}

	// 로그인 & 
	@RequestMapping("/userSelect")
//	public void userSelect(HttpServletRequest req, HttpServletResponse res, RedirectAttributes attr, HttpSession session) {
	public ModelAndView userSelect(HttpServletRequest req, HttpServletResponse res, RedirectAttributes attr, HttpSession session) {
		HashMap<String, Object> param = HttpUtil.getParamMap(req);
		param.put("sqlType", "user.userSelect");
		param.put("sql", "selectOne");

		System.out.println("userSelect :  " + param);
		HashMap<String, Object> resultMap = (HashMap<String, Object>) di.call(param);
		if (resultMap == null) {
			// 로그인되지 않음
			resultMap = new HashMap<String, Object>();
			resultMap.put("status", FinalUtil.NO);
			resultMap.put("msg", "이메일 또는 비번이 틀립니다");
			
		} else {
			resultMap.put("status", FinalUtil.OK);
			resultMap.put("msg", "로그인 되었습니다.");
		}
		session.setAttribute("user", resultMap);
		return HttpUtil.makeJsonView(resultMap);
	}
	
	
	// 정보수정
		@RequestMapping("/userUpdate")
		public ModelAndView userUpdate(HttpServletRequest req) {
			String userEmail =  req.getParameter("userEmail");
			String userName =  req.getParameter("userName");
			String userPwd =  req.getParameter("userPwd");
			String addrNo =  req.getParameter("addrNo");
			String addr =  req.getParameter("addr");
			
			System.out.println("para m = = = = = "  + userName +userName);
			System.out.println("para m = = = = = "  + userPwd +addrNo);
			System.out.println("para m = = = = = "  + addr );
			HashMap<String, Object> param = HttpUtil.getParamMap(req);
			HashMap<String, Object> map = new HashMap<String, Object>();
			System.out.println("test  __update  " + param);
			
			param.put("sqlType", "user.userUpdate");
			param.put("sql", "update");
			
			int status = (int) di.call(param);
			/********************************************************************************************/
			System.out.println(status);
				
			if (status == 1) {
				map.put("msg", "글수정이 완료 되었습니다.");
				map.put("status", FinalUtil.OK);
				
			} else {
				map.put("msg", "글 작성 시 오류 발생.");
			}
			
			return HttpUtil.makeJsonView(map);
		}
	
	//login check (정보 확인하기)
	@RequestMapping("/userCheck")
	public void userCheck(HttpServletResponse res, HttpSession session) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		if(session == null) {
			resultMap.put("status", FinalUtil.OK);
		}else {
			resultMap = (HashMap<String, Object>) session.getAttribute("user");
			if(resultMap == null) {
				resultMap = new HashMap<String, Object>();
				resultMap.put("status", FinalUtil.NO);
			}else {				
				resultMap.put("status", FinalUtil.OK);
			}			
		}		
		HttpUtil.makeJsonWriter(res, resultMap);
	}
	
	//logout
	@RequestMapping("/logout")
	public ModelAndView logout(HttpServletResponse res, HttpSession session) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("status", FinalUtil.NO);
		resultMap.put("msg", "로그아웃 되었습니다");
		session.invalidate();
		return HttpUtil.makeJsonView(resultMap);
	}

}
