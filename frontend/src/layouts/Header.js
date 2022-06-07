import React, { useState, useEffect } from 'react'
import {Link, useNavigate} from 'react-router-dom'
import {Navbar, Container, Nav, Button} from 'react-bootstrap'

import {TOKEN} from '../api/token'
import { signOut } from '../api/ApiService'
import SignUp from "../page/member/SignUp";
import SignIn from "../page/member/SignIn";


const Header = () => {
	const navigate = useNavigate();

	const [signUpModalOn, setSignUpModalOn] = useState(false)
	const [signInModalOn, setSignInModalOn] = useState(false)
	const [signUpShow, setSignUpShow] = useState(true)
	const [signInShow, setSignInShow] = useState(true)
	const [signOutShow, setSignOutShow] = useState(false)

	const signInButtonHandler = () => {
		setSignUpShow(false)
		setSignInShow(false)
		setSignOutShow(true)
	}

	const signOutButtonHandler = () => {
		signOut()
		setSignUpShow(true)
		setSignInShow(true)
		setSignOutShow(false)
	}

	useEffect(() => {
		if (sessionStorage.getItem(TOKEN) === 'null') {
			setSignUpShow(true)
			setSignInShow(true)
			setSignOutShow(false)
		}
		else {
			setSignUpShow(false)
			setSignInShow(false)
			setSignOutShow(true)
		}
	})

	const myPageHanlder = () => {
		if (sessionStorage.getItem(TOKEN)) {
			navigate(`/mypage`);
		}
		else {
			alert('로그인이 필요합니다')
		}
	}

	return (
		<>
			<header>
				<Navbar bg="light" expand="lg">
					<Container>
						<Navbar.Brand href="/">Level Up</Navbar.Brand>
						<Navbar.Toggle aria-controls="basic-navbar-nav" />
						<Navbar.Collapse className="justify-content-end">
							<Nav className="ml-auto">
								{
									signUpShow &&
									<Nav.Link>
										<Link to='/signup'>
											<Button size="md">
												회원가입
											</Button>
										</Link>
									</Nav.Link>
								}
								{
									signInShow &&
									<Nav.Link>
										<Link to='/signin'>
											<Button size="md">
												로그인
											</Button>
										</Link>
									</Nav.Link>
								}
								{
									signOutShow &&
									<Nav.Link>
										<Button onClick={signOutButtonHandler} size="md">
											로그아웃
										</Button>
									</Nav.Link>
								}
								<Nav.Link>
									<Button onClick={myPageHanlder} size="md">
										마이페이지
									</Button>
								</Nav.Link>
								<Nav.Link>
									<Button size="md">
										QNA
									</Button>
								</Nav.Link>
								<Nav.Link>
									<Button size="md">
										공지사항
									</Button>
								</Nav.Link>
							</Nav>
						</Navbar.Collapse>
					</Container>
				</Navbar>
			</header>
		</>
  )
}

export default Header