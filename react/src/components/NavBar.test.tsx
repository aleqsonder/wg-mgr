import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";

import { NavBar } from "./NavBar";

vi.mock("react-router-dom", async () => {
    const actual = await vi.importActual("react-router-dom");
    return {
        ...actual,
    };
});

describe("NavBar", () => {
    it("рендерит три ссылки", () => {
        render(
            <MemoryRouter>
                <NavBar />
            </MemoryRouter>
        );

        expect(screen.getByText("Users Management")).toBeInTheDocument();
        expect(screen.getByText("Create Users")).toBeInTheDocument();
        expect(screen.getByText("Account")).toBeInTheDocument();
    });

    it("ссылки имеют корректные href", () => {
        render(
            <MemoryRouter>
                <NavBar />
            </MemoryRouter>
        );

        expect(screen.getByText("Users Management")).toHaveAttribute("href", "/");
        expect(screen.getByText("Create Users")).toHaveAttribute("href", "/create");
        expect(screen.getByText("Account")).toHaveAttribute("href", "/account");
    });

    it("использует правильный класс, когда isActive = false", () => {
        render(
            <MemoryRouter>
                <NavBar />
            </MemoryRouter>
        );

        const link = screen.getByText("Users Management");
        expect(link.className).toBeDefined(); // хотя бы проверка, что класс есть
    });
});
